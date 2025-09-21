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
import { useRegister } from '@/hooks/auth'

const passwordErrorMessage =
	'Password must be at least 8 characters and contain both a letter and digit'

const formSchema = z
	.object({
		firstName: z.string(),
		lastName: z.string(),
		email: z.email(),
		password: z
			.string()
			.min(8, passwordErrorMessage)
			.regex(/^(?=.*[A-z])(?=.*\d).+$/, passwordErrorMessage),
		confirmPassword: z.string(),
	})
	.refine((data) => data.password === data.confirmPassword, {
		path: ['confirmPassword'],
		error: 'Passwords do not match',
	})

type FormSchema = z.infer<typeof formSchema>

const formFields = [
	{ name: 'firstName', type: 'text', label: 'First name', placeholder: 'John' },
	{ name: 'lastName', type: 'text', label: 'Last name', placeholder: 'Doe' },
	{ name: 'email', type: 'email', label: 'Email', placeholder: 'john.doe@mail.com' },
	{ name: 'password', type: 'password', label: 'Password', placeholder: '••••••••••••' },
	{
		name: 'confirmPassword',
		type: 'password',
		label: 'Confirm password',
		placeholder: '••••••••••••',
	},
] as const

type FormFieldName = (typeof formFields)[number]['name']

export function RegisterForm() {
	const register = useRegister()

	const form = useForm<FormSchema>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			firstName: '',
			lastName: '',
			email: '',
			password: '',
			confirmPassword: '',
		},
	})

	const navigate = useNavigate()

	async function handleSubmit(data: FormSchema) {
		const response = await register.trigger({
			firstName: data.firstName,
			lastName: data.lastName,
			email: data.email,
			password: data.password,
		})

		if (response.errors.length > 0) {
			for (const error of response.errors) {
				form.setError((error.field as FormFieldName) ?? 'root', {
					message: error.message,
					type: 'value',
				})
			}
			return
		}

		navigate('/login')
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

				<Button type='submit' className='w-full' disabled={register.isMutating}>
					Sign up
				</Button>

				<div className='flex justify-center'>
					<p>
						Already have an account?{' '}
						<Button variant='link' asChild className='p-0'>
							<Link to='/login'>Log in</Link>
						</Button>
					</p>
				</div>
			</form>
		</Form>
	)
}
