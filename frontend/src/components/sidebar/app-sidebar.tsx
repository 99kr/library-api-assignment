import { BookText, FlaskConical, LogOut, SunMoon, SwatchBook } from 'lucide-react'
import { Link, useLocation } from 'react-router'
import { AppSidebarGroup, type Group } from '@/components/sidebar/app-sidebar-group'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Sidebar, SidebarContent, SidebarFooter, SidebarHeader } from '@/components/ui/sidebar'
import { useJwt } from '@/hooks/state/useJwt'
import { useTheme } from '@/hooks/state/useTheme'
import { cn } from '@/lib/utils'

const groups: Group[] = [
	{
		name: 'Application',
		role: 'USER',
		items: [{ name: 'Books', href: '/books', icon: BookText }],
	},
	{
		name: 'Administration',
		role: 'ADMIN',
		items: [{ name: 'Test', href: '/test', icon: FlaskConical }],
	},
]

export function AppSidebar() {
	const location = useLocation()
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
				{groups.map((group) => (
					<AppSidebarGroup group={group} key={group.name} />
				))}
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
							<Badge variant='secondary' className='mb-1 capitalize'>
								{jwt.getMostPrivilegedRole().toLowerCase()}
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
