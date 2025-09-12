import { Outlet } from 'react-router'
import { AppSidebar } from '@/components/sidebar/app-sidebar'
import { SidebarProvider } from '@/components/ui/sidebar'

export function Layout() {
	return (
		<>
			<SidebarProvider>
				<AppSidebar />
				<main className='mx-12 my-6 w-full h-[100vh-calc(var(--spacing)*6*2)]'>
					<Outlet />
				</main>
			</SidebarProvider>
		</>
	)
}
