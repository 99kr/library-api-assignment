import type { LucideIcon } from 'lucide-react'
import { Link, useLocation } from 'react-router'
import {
	SidebarGroup,
	SidebarGroupContent,
	SidebarGroupLabel,
	SidebarMenu,
	SidebarMenuButton,
	SidebarMenuItem,
} from '@/components/ui/sidebar'
import { type Role, useJwt } from '@/hooks/state/useJwt'

export type Group = {
	name: string
	role: Role | null
	items: {
		name: string
		href: string
		icon: LucideIcon
	}[]
}

export function AppSidebarGroup({ group }: { group: Group }) {
	const hasRole = useJwt((state) => state.hasRole)
	const location = useLocation()

	if (group.role && !hasRole(group.role)) return null

	return (
		<SidebarGroup>
			<SidebarGroupLabel>{group.name}</SidebarGroupLabel>
			<SidebarGroupContent>
				<SidebarMenu>
					{group.items.map((item) => (
						<SidebarMenuItem key={item.name}>
							<SidebarMenuButton asChild isActive={location.pathname === item.href}>
								<Link to={item.href}>
									<item.icon className='size-4' />
									<span>{item.name}</span>
								</Link>
							</SidebarMenuButton>
						</SidebarMenuItem>
					))}
				</SidebarMenu>
			</SidebarGroupContent>
		</SidebarGroup>
	)
}
