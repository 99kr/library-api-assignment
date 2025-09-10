import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router'
import z from 'zod'
import { Button } from '@/components/ui/button'
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { useLogin } from '@/hooks/api/useLogin'
import { useJwt } from '@/hooks/state/useJwt'
import { setRefreshTokenState } from '@/lib/refreshTokenState'

const formSchema = z.object({
	email: z.email(),
	password: z.string(),
})

type FormSchema = z.infer<typeof formSchema>

const formFields = [
	{ name: 'email', type: 'email', label: 'Email', placeholder: 'john.doe@mail.com' },
	{ name: 'password', type: 'password', label: 'Password', placeholder: '••••••••••••' },
] as const

export function LoginForm() {
	const login = useLogin()
	const jwt = useJwt()

	const form = useForm<FormSchema>({
		resolver: zodResolver(formSchema),
		defaultValues: { email: '', password: '' },
	})

	const navigate = useNavigate()

	async function handleSubmit(data: FormSchema) {
		const response = await login.trigger(data)

		if (response.errors.length > 0) {
			form.setError('email', { message: 'Invalid credentials', type: 'value' })
			form.setError('password', { message: 'Invalid credentials', type: 'value' })
			return
		}

		jwt.setAccessToken(response.data.accessToken)
		jwt.setIdentityFromJwtToken(response.data.accessToken)

		setRefreshTokenState({
			maxAge: response.data.refreshTokenDurationMs,
			expiresAt: Date.now() + response.data.refreshTokenDurationMs,
		})

		navigate('/')
	}

	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(handleSubmit)} className='space-y-8'>
				{formFields.map((formField) => (
					<FormField
						key={formField.name}
						control={form.control}
						name={formField.name}
						render={({ field }) => (
							<FormItem>
								<FormLabel>{formField.label}</FormLabel>
								<FormControl>
									<Input
										placeholder={formField.placeholder}
										type={formField.type}
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
				))}

				<Button type='submit' className='w-full' disabled={login.isMutating}>
					Log in
				</Button>

				<div className='flex justify-center'>
					<p>
						Don't have an account?{' '}
						<Button variant='link' asChild className='p-0'>
							<Link to='/signup'>Sign up</Link>
						</Button>
					</p>
				</div>
			</form>
		</Form>
	)
}
