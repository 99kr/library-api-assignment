import { Link, useNavigate, useSearchParams } from 'react-router'
import { Button } from '@/components/ui/button'
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { useLogout } from '@/hooks/api/useLogout'
import { loggedOutState, useSelf } from '@/hooks/api/useSelf'

export function Logout() {
	const logout = useLogout()
	const navigate = useNavigate()
	const [params] = useSearchParams()
	const { mutate: mutateSelf } = useSelf()

	const from = params.get('from') ?? '/'

	const handleLogout = async () => {
		await logout.trigger()

		//mutateSelf(undefined)
		mutateSelf(loggedOutState) // Seems to be some race condition, so mutate to force a refresh

		navigate('/')
	}

	return (
		<main className='flex flex-col min-h-screen items-center justify-center'>
			<Card className='w-full max-w-sm'>
				<CardHeader>
					<CardTitle>Logout</CardTitle>
					<CardDescription>Are you sure you want to log out?</CardDescription>
				</CardHeader>
				<CardFooter className='flex flex-row gap-2 *:flex-1'>
					<Button variant='outline' asChild>
						<Link to={from}>Go back</Link>
					</Button>
					<Button variant='destructive' onClick={handleLogout}>
						Log out
					</Button>
				</CardFooter>
			</Card>
		</main>
	)
}
