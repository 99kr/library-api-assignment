import { useEffect } from 'react'
import { RouterProvider } from 'react-router'
import type { SWRConfiguration } from 'swr'
import { SWRConfig } from 'swr'
import { useJwt } from '@/hooks/state/useJwt'
import { getAccessTokenFromRefreshToken } from '@/lib/api'
import { hasRefreshTokenState, removeRefreshTokenState } from '@/lib/refreshTokenState'
import { router } from '@/router'

export function App() {
	const jwt = useJwt()

	useEffect(() => {
		if (jwt.accessToken) return

		const handleInitialRefreshTry = async () => {
			const hasRefreshToken = hasRefreshTokenState()
			if (!hasRefreshToken) {
				return jwt.setIdentityAsLoggedOut()
			}

			const accessToken = await getAccessTokenFromRefreshToken()
			// Refresh token is invalid
			if (accessToken === null) {
				removeRefreshTokenState()
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

	const handleOnErrorRetry: SWRConfiguration['onErrorRetry'] = async (
		error,
		_key,
		_config,
		revalidate,
	) => {
		if (error.status === 401) {
			const hasRefreshToken = hasRefreshTokenState()
			if (!hasRefreshToken) {
				return router.navigate('/login')
			}

			const accessToken = await getAccessTokenFromRefreshToken()
			// Refresh token was invalid
			if (accessToken === null) {
				jwt.setAccessToken(null)
				removeRefreshTokenState()

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
	}

	return (
		<SWRConfig
			value={{
				revalidateOnFocus: false,
				onErrorRetry: handleOnErrorRetry,
			}}
		>
			<RouterProvider router={router} />
		</SWRConfig>
	)
}
