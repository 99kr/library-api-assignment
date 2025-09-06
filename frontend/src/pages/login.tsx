import { SwatchBook } from 'lucide-react'
import { LoginForm } from '@/components/forms/login-form'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export function Login() {
	return (
		<main className='flex flex-col min-h-screen items-center justify-center'>
			<Card className='w-full max-w-sm space-y-2'>
				<CardHeader className='text-center'>
					<SwatchBook className='size-8 mx-auto text-base-400' />
					<CardTitle className='text-2xl'>Welcome back</CardTitle>
					<CardDescription>Log in with your account</CardDescription>
				</CardHeader>
				<CardContent>
					<LoginForm />
				</CardContent>
			</Card>
		</main>
	)
}
