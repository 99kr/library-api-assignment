import { type PropsWithChildren, useState } from 'react'
import { CreateBookForm } from '@/components/forms/create-book-form'
import { Button } from '@/components/ui/button'
import {
	Dialog,
	DialogClose,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from '@/components/ui/dialog'

export function CreateBookDialog({ children }: PropsWithChildren) {
	const [open, setOpen] = useState(false)

	function close() {
		setOpen(false)
	}

	return (
		<Dialog open={open} onOpenChange={setOpen}>
			<DialogTrigger asChild>{children}</DialogTrigger>
			<DialogContent>
				<DialogHeader>
					<DialogTitle>Add book to the library</DialogTitle>
					<DialogDescription>
						Lorem ipsum dolor sit amet, consectetur adipiscing elit.
					</DialogDescription>
				</DialogHeader>

				<CreateBookForm close={close} />

				<DialogFooter>
					<DialogClose asChild>
						<Button variant='outline'>Cancel</Button>
					</DialogClose>
					<Button type='submit' form='create-book-form'>
						Add book
					</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	)
}
