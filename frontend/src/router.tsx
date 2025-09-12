import { createBrowserRouter, createRoutesFromElements, Route } from 'react-router'
import { AccessDenied } from '@/pages/access-denied'
import { Books } from '@/pages/books'
import { Home } from '@/pages/home'
import { Layout } from '@/pages/layout'
import { Login } from '@/pages/login'
import { Logout } from '@/pages/logout'
import { NotFound } from '@/pages/not-found'
import { Register } from '@/pages/register'

export const router = createBrowserRouter(
	createRoutesFromElements(
		<>
			<Route element={<Layout />}>
				<Route path='/' element={<Home />} />
				<Route path='/books' element={<Books />} />
				<Route path='/denied' element={<AccessDenied />} />
				<Route path='*' element={<NotFound />} />
			</Route>

			<Route path='/login' element={<Login />} />
			<Route path='/logout' element={<Logout />} />
			<Route path='/register' element={<Register />} />
		</>,
	),
)
