import { BookText, ScrollText, SunMoon, SwatchBook } from 'lucide-react'
import { Link } from 'react-router'
import { AppSidebarGroup, type Group } from '@/components/sidebar/app-sidebar-group'
import { ProfileDropdown } from '@/components/sidebar/profile-dropdown'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Sidebar, SidebarContent, SidebarFooter, SidebarHeader } from '@/components/ui/sidebar'
import { useJwt } from '@/hooks/state/useJwt'
import { useTheme } from '@/hooks/state/useTheme'

const groups: Group[] = [
	{
		items: [{ name: 'Books', href: '/books', icon: BookText }],
	},
	{
		name: 'Administration',
		role: 'ADMIN',
		items: [{ name: 'Audit logs', href: '/logs', icon: ScrollText }],
	},
]

export function AppSidebar() {
	const toggleTheme = useTheme((state) => state.toggleTheme)
	const { identity, ...jwt } = useJwt()

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
				{groups.map((group, i) => (
					<AppSidebarGroup group={group} key={group.name ?? i} />
				))}
			</SidebarContent>
			<SidebarFooter>
				{!identity ? (
					<Button asChild variant='outline'>
						<Link to='/login'>Log in</Link>
					</Button>
				) : (
					<ProfileDropdown>
						<div className='flex gap-2 items-center justify-between p-2 rounded-xl border border-transparent hover:cursor-pointer hover:bg-background/30 hover:border-inherit transition-colors duration-100'>
							<div className='flex flex-col'>
								<Badge variant='secondary' className='mb-1 capitalize'>
									{jwt.getMostPrivilegedRole().toLowerCase()}
								</Badge>
								<p className='font-medium'>
									{identity.firstName} {identity.lastName}
								</p>
								<p className='text-sm text-base-400'>{identity.email}</p>
							</div>
						</div>
					</ProfileDropdown>
				)}
			</SidebarFooter>
		</Sidebar>
	)
}
