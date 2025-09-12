import { SwatchBook } from 'lucide-react'
import { RegisterForm } from '@/components/forms/register-form'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export function Register() {
	return (
		<main className='flex flex-col min-h-screen items-center justify-center'>
			<Card className='w-full max-w-sm space-y-2'>
				<CardHeader className='text-center'>
					<SwatchBook className='size-8 mx-auto text-base-400' />
					<CardTitle className='text-2xl'>Welcome</CardTitle>
					<CardDescription>Register an account</CardDescription>
				</CardHeader>
				<CardContent>
					<RegisterForm />
				</CardContent>
			</Card>
		</main>
	)
}
