import { useEffect } from 'react'
import { createBrowserRouter, RouterProvider } from 'react-router'
import { SWRConfig } from 'swr'
import { useJwt } from '@/hooks/state/useJwt'
import { getAccessTokenFromRefreshToken } from '@/lib/api'
import { AccessDenied } from '@/pages/access-denied'
import { Books } from '@/pages/books'
import { Home } from '@/pages/home'
import { Layout } from '@/pages/layout'
import { Login } from '@/pages/login'
import { Logout } from '@/pages/logout'
import { NotFound } from '@/pages/not-found'

export const router = createBrowserRouter([
	{
		element: <Layout />,
		children: [
			{ path: '/', element: <Home /> },
			{ path: '/books', element: <Books /> },
			{ path: '/denied', element: <AccessDenied /> },
			{ path: '*', element: <NotFound /> },
		],
	},
	{ path: '/login', element: <Login /> },
	{ path: '/logout', element: <Logout /> },
])

export function App() {
	const jwt = useJwt()

	useEffect(() => {
		if (jwt.accessToken) return

		const handleInitialRefreshTry = async () => {
			// Possibly keep maxAge of refresh token in localStorage, and log out user if it's expired

			const hasRefreshToken = localStorage.getItem('has_refresh_token')
			if (!hasRefreshToken) {
				return jwt.setIdentityAsLoggedOut()
			}

			const accessToken = await getAccessTokenFromRefreshToken()
			// Refresh token is invalid
			if (accessToken === null) {
				localStorage.removeItem('has_refresh_token')
				return jwt.setIdentityAsLoggedOut()
			}

			jwt.setAccessToken(accessToken)
			jwt.setIdentityFromJwtToken(accessToken)
		}

		handleInitialRefreshTry()
	}, [
		jwt.accessToken,
		jwt.setAccessToken,
		jwt.setIdentityAsLoggedOut,
		jwt.setIdentityFromJwtToken,
	])

	return (
		<SWRConfig
			value={{
				revalidateOnFocus: false,
				onErrorRetry: async (error, _key, _config, revalidate) => {
					if (error.status === 401) {
						const hasRefreshToken = localStorage.getItem('has_refresh_token')
						if (!hasRefreshToken) {
							return router.navigate('/login')
						}

						const accessToken = await getAccessTokenFromRefreshToken()
						// Refresh token was invalid
						if (accessToken === null) {
							jwt.setAccessToken(null)
							localStorage.removeItem('has_refresh_token')

							return router.navigate('/login')
						}

						jwt.setAccessToken(accessToken)
						jwt.setIdentityFromJwtToken(accessToken)
					}

					if (error.status === 403) {
						return router.navigate('/denied')
					}

					if (error.status === 404) {
						return router.navigate('/not-found')
					}

					revalidate()
				},
			}}
		>
			<RouterProvider router={router} />
		</SWRConfig>
	)
}
