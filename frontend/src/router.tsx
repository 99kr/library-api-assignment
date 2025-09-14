import { createBrowserRouter, createRoutesFromElements, Navigate, Route } from 'react-router'
import { useJwt } from '@/hooks/state/useJwt'
import { AccessDenied } from '@/pages/access-denied'
import { Books } from '@/pages/books'
import { Home } from '@/pages/home'
import { Layout } from '@/pages/layout'
import { Login } from '@/pages/login'
import { Logout } from '@/pages/logout'
import { Logs } from '@/pages/logs'
import { NotFound } from '@/pages/not-found'
import { Register } from '@/pages/register'

export const router = createBrowserRouter(
	createRoutesFromElements(
		<>
			<Route element={<Layout />}>
				<Route path='/' element={<Home />} />
				<Route path='/books' element={<Books />} />

				<Route path='/logs' element={<Logs />} />

				<Route path='/denied' element={<AccessDenied />} />
				<Route path='*' element={<NotFound />} />
			</Route>

			<Route path='/login' element={<Login />} />
			<Route path='/logout' element={<Logout />} />
			<Route path='/register' element={<Register />} />
		</>,
	),
)
