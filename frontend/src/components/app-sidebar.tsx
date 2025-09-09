import { BookText, LogOut, SunMoon, SwatchBook } from 'lucide-react'
import { Link, useLocation } from 'react-router'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import {
	Sidebar,
	SidebarContent,
	SidebarFooter,
	SidebarGroup,
	SidebarGroupContent,
	SidebarGroupLabel,
	SidebarHeader,
	SidebarMenu,
	SidebarMenuButton,
	SidebarMenuItem,
} from '@/components/ui/sidebar'
import { useJwt } from '@/hooks/state/useJwt'
import { useTheme } from '@/hooks/state/useTheme'
import { cn } from '@/lib/utils'

const items = [{ name: 'Books', href: '/books', icon: BookText }]

export function AppSidebar() {
	const location = useLocation()
	const toggleTheme = useTheme((state) => state.toggleTheme)
	const identity = useJwt((state) => state.identity)

	return (
		<Sidebar variant='floating'>
			<SidebarHeader className='flex-row justify-between items-center mt-2'>
				<Link to='/' className='flex items-center gap-2'>
					<SwatchBook className='size-5 ml-2 text-card-foreground/80' />
					<span className='text-lg font-bold'>Library</span>
				</Link>
				<Button variant='ghost' size='icon' onClick={toggleTheme}>
					<SunMoon className='size-4' />
				</Button>
			</SidebarHeader>
			<SidebarContent>
				<SidebarGroup>
					<SidebarGroupLabel>User</SidebarGroupLabel>
					<SidebarGroupContent>
						<SidebarMenu>
							{items.map((item) => (
								<SidebarMenuItem key={item.name}>
									<SidebarMenuButton
										asChild
										isActive={location.pathname === item.href}
									>
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
			</SidebarContent>
			<SidebarFooter
				className={cn(
					'transition-opacity opacity-0 ease-in-out',
					identity && 'opacity-100',
				)}
			>
				{!identity?.isLoggedIn ? (
					<Button asChild variant='outline'>
						<Link to='/login'>Log in</Link>
					</Button>
				) : (
					<div className='flex gap-2 items-center justify-between p-2'>
						<div className='flex flex-col'>
							<Badge variant='secondary' className='mb-1'>
								Role
							</Badge>
							<p className='font-medium'>
								{identity.firstName} {identity.lastName}
							</p>
							<p className='text-sm text-base-400'>{identity.email}</p>
						</div>

						<Button variant='ghost' size='icon' asChild>
							<Link to={`/logout?from=${location.pathname}`}>
								<LogOut className='h-4 w-4' />
							</Link>
						</Button>
					</div>
				)}
			</SidebarFooter>
		</Sidebar>
	)
}
