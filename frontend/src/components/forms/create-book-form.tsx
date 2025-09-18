import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import z from 'zod'
import { AuthorCombobox } from '@/components/comboboxes/author-combobox'
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import type { Author } from '@/hooks/api/authors/useAuthorsQuery'
import { useBooksMutation } from '@/hooks/api/books/useBooksMutation'

const formSchema = z
	.object({
		title: z.string().trim().nonempty('Title is required'),
		author: z
			.object({
				id: z.number(),
				firstName: z.string(),
				lastName: z.string(),
				birthYear: z.number(),
				nationality: z.string(),
			})
			.refine((data) => data.id !== -1, 'Author is required'),
		publicationYear: z
			.number()
			.positive('Publication year must be a positive number')
			.or(z.string().max(0)) // Allow default as empty string, so we can backspace without automatically setting it to 0
			.refine((publicationYear) => publicationYear !== '', 'Publication year is required'),
		availableCopies: z
			.number()
			.positive('Available copies must be a positive number')
			.or(z.string().max(0))
			.refine((availableCopies) => availableCopies !== '', 'Available copies is required'),
		totalCopies: z
			.number()
			.positive('Total copies must be a positive number')
			.or(z.string().max(0))
			.refine((totalCopies) => totalCopies !== '', 'Total copies is required'),
	})
	.refine((data) => data.availableCopies <= data.totalCopies, {
		path: ['availableCopies'],
		message: 'Available copies cannot be greater than total copies',
	})

type FormSchema = z.infer<typeof formSchema>

const formFields = [
	{ name: 'title', type: 'text', label: 'Title', placeholder: 'The Lord of the Rings' },
	{ name: 'author', type: 'author', label: 'Author', placeholder: 'J. R. R. Tolkien' },
	{ name: 'publicationYear', type: 'number', label: 'Publication year', placeholder: '1954' },
	{ name: 'availableCopies', type: 'number', label: 'Available copies', placeholder: '100' },
	{ name: 'totalCopies', type: 'number', label: 'Total copies', placeholder: '200' },
] as const

type FormFieldName = (typeof formFields)[number]['name']

// Composed of `name` of `formFields`
const gridTemplate = `
    "title title"
    "author publicationYear"
    "availableCopies totalCopies"
`

export function CreateBookForm({ close }: { close: () => void }) {
	const booksMutation = useBooksMutation()

	const form = useForm<FormSchema>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			title: '',
			author: { id: -1, firstName: '', lastName: '', birthYear: 0, nationality: '' },
			publicationYear: '',
			availableCopies: '',
			totalCopies: '',
		},
	})

	async function handleSubmit(data: FormSchema) {
		if (typeof data.publicationYear !== 'number') return
		if (typeof data.availableCopies !== 'number') return
		if (typeof data.totalCopies !== 'number') return
		if (data.author.id === -1) return

		const response = await booksMutation.trigger({
			title: data.title,
			authorId: data.author.id,
			publicationYear: data.publicationYear,
			availableCopies: data.availableCopies,
			totalCopies: data.totalCopies,
		})

		if (response.errors.length > 0) {
			for (const error of response.errors) {
				let fieldName = error.field
				if (fieldName === 'authorId') fieldName = 'author'

				form.setError((error.field as FormFieldName) ?? 'root', {
					message: error.message,
					type: 'value',
				})
			}
			return
		}

		close()
	}

	return (
		<Form {...form}>
			<form
				onSubmit={form.handleSubmit(handleSubmit)}
				id='create-book-form'
				className='my-4 grid grid-cols-2 gap-x-4 gap-y-8 items-baseline'
				style={{ gridTemplate }}
			>
				{formFields.map((formField) => (
					<FormField
						key={formField.name}
						control={form.control}
						name={formField.name}
						render={({ field }) => (
							<FormItem style={{ gridArea: formField.name }}>
								<FormLabel>{formField.label}</FormLabel>
								{formField.type === 'author' ? (
									<AuthorCombobox
										setAuthor={(author) => form.setValue('author', author)}
										author={field.value as Author}
									/>
								) : (
									<FormControl>
										<Input
											placeholder={formField.placeholder}
											type={formField.type}
											value={field.value as string | number}
											onChange={({ target }) => {
												const value =
													formField.type === 'number'
														? target.valueAsNumber
														: target.value

												form.setValue(
													formField.name,
													Number.isNaN(value) ? '' : value,
												)
											}}
										/>
									</FormControl>
								)}
								<FormMessage />
							</FormItem>
						)}
					/>
				))}
			</form>
		</Form>
	)
}
