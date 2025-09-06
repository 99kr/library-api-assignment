import { Outlet } from 'react-router'
import { AppSidebar } from '@/components/app-sidebar'
import { SidebarProvider } from '@/components/ui/sidebar'
import { useSelf } from '@/hooks/api/useSelf'

export function Layout() {
	const { data: self, isLoading } = useSelf()

	return (
		<>
			<SidebarProvider>
				<AppSidebar self={self} isLoading={isLoading} />
				<main className='mx-12 my-6 w-full'>
					<Outlet />
				</main>
			</SidebarProvider>
		</>
	)
}
