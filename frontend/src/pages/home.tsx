import { useEffect, useRef } from 'react'
import { parsePayloadFromJwt, useJwt } from '@/hooks/state/useJwt'
import { millisecondsToReadableTime } from '@/lib/utils'

export function Home() {
	const jwtToken = useJwt()
	const expirationTimeRef = useRef<HTMLParagraphElement>(null)
	const payload = jwtToken.accessToken ? parsePayloadFromJwt(jwtToken.accessToken) : null

	useEffect(() => {
		const interval = setInterval(() => {
			if (!expirationTimeRef.current || !payload) return

			const ms = payload.exp * 1000 - Date.now()

			expirationTimeRef.current.innerText =
				ms > 0 ? millisecondsToReadableTime(ms) : 'Expired'
		}, 1000)

		return () => clearInterval(interval)
	}, [payload])

	return (
		<>
			<h1 className='text-4xl font-semibold'>Home</h1>

			<h2 className='mt-8 mb-2 text-2xl font-semibold'>JWT Access Token</h2>
			<pre className='text-wrap break-all'>{jwtToken.accessToken ?? 'No token'}</pre>

			{payload && (
				<>
					<hr className='my-8 border-none bg-card w-full h-px' />
					<h2 className='mt-8 mb-2 text-2xl font-semibold'>
						Decoded payload from JWT Access Token
					</h2>
					<pre className='text-wrap break-all'>{JSON.stringify(payload, null, 4)}</pre>

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
							<p>{millisecondsToReadableTime((payload.exp - payload.iat) * 1000)}</p>
						</div>
						<div>
							<h3 className='text-xl font-semibold'>Time till expiration</h3>
							<p ref={expirationTimeRef}>00:00</p>
						</div>
					</div>
				</>
			)}
		</>
	)
}
