import { BrowserRouter, Route, Routes } from 'react-router'
import { SWRConfig } from 'swr'
import { Books } from '@/pages/books'
import { Home } from '@/pages/home'
import { Layout } from '@/pages/layout'
import { Login } from '@/pages/login'
import { Logout } from '@/pages/logout'

export function App() {
	return (
		<SWRConfig
			value={{
				revalidateOnFocus: false,
			}}
		>
			<BrowserRouter>
				<Routes>
					<Route element={<Layout />}>
						<Route path='/' element={<Home />} />
						<Route path='/books' element={<Books />} />
					</Route>

					<Route path='/login' element={<Login />} />
					<Route path='/logout' element={<Logout />} />
				</Routes>
			</BrowserRouter>
		</SWRConfig>
	)
}
