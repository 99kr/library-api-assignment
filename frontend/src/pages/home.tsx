import { useEffect, useRef } from 'react'
import { parsePayloadFromJwt, useJwt } from '@/hooks/state/useJwt'
import { getRefreshTokenState } from '@/lib/refreshTokenState'
import { msToDaysHoursMinutesAndSeconds, msToMinutesAndSeconds } from '@/lib/utils'

export function Home() {
	const jwt = useJwt()
	const accessExpirationTimeRef = useRef<HTMLParagraphElement>(null)
	const refreshExpirationTimeRef = useRef<HTMLParagraphElement>(null)
	const payload = jwt.accessToken ? parsePayloadFromJwt(jwt.accessToken) : null

	useEffect(() => {
		const interval = setInterval(() => {
			if (!accessExpirationTimeRef.current || !payload) return

			const ms = payload.exp * 1000 - Date.now()

			accessExpirationTimeRef.current.innerText =
				ms > 0 ? msToMinutesAndSeconds(ms) : 'Expired'
		}, 1000)

		return () => clearInterval(interval)
	}, [payload])

	useEffect(() => {
		const interval = setInterval(() => {
			const refreshTokenState = getRefreshTokenState()
			if (!refreshExpirationTimeRef.current || !refreshTokenState) return

			const ms = refreshTokenState.expiresAt - Date.now()

			refreshExpirationTimeRef.current.innerText =
				ms > 0 ? msToDaysHoursMinutesAndSeconds(ms) : 'Expired'
		}, 1000)

		return () => clearInterval(interval)
	}, [])

	return (
		<>
			<h1 className='text-4xl font-semibold'>Home</h1>

			<h2 className='mt-8 mb-2 text-2xl font-semibold'>JWT Access Token</h2>
			<pre className='text-wrap break-all'>{jwt.accessToken ?? 'No token'}</pre>

			{payload && (
				<div className='flex *:flex-1 border-t border-card mt-8'>
					<div>
						<h2 className='mt-8 mb-2 text-2xl font-semibold'>
							Decoded payload from JWT Access Token
						</h2>
						<pre className='text-wrap break-all'>
							{JSON.stringify(payload, null, 4)}
						</pre>

						<div className='flex gap-6 mt-6'>
							<div>
								<h3 className='text-xl font-semibold'>Issued at</h3>
								<p>{new Date(payload.iat * 1000).toLocaleString('sv-SE')}</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Expires at</h3>
								<p>{new Date(payload.exp * 1000).toLocaleString('sv-SE')}</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Time to live</h3>
								<p>{msToMinutesAndSeconds((payload.exp - payload.iat) * 1000)}</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Time till expiration</h3>
								<p ref={accessExpirationTimeRef}>00:00</p>
							</div>
						</div>
					</div>

					<div>
						<h2 className='mt-8 mb-2 text-2xl font-semibold'>
							JWT Refresh Token state
						</h2>

						<div className='flex gap-6 mt-6'>
							<div>
								<h3 className='text-xl font-semibold'>Issued at</h3>
								<p>
									{new Date(
										(getRefreshTokenState()?.expiresAt ?? 0) -
											(getRefreshTokenState()?.maxAge ?? 0),
									).toLocaleString('sv-SE')}
								</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Expires at</h3>
								<p>
									{new Date(
										getRefreshTokenState()?.expiresAt ?? 0,
									).toLocaleString('sv-SE')}
								</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Time to live</h3>
								<p>
									{msToDaysHoursMinutesAndSeconds(
										getRefreshTokenState()?.maxAge ?? 0,
									)}
								</p>
							</div>
							<div>
								<h3 className='text-xl font-semibold'>Time till expiration</h3>
								<p ref={refreshExpirationTimeRef}>00:00</p>
							</div>
						</div>
					</div>
				</div>
			)}
		</>
	)
}
