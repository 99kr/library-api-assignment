import { useDebounce } from '@uidotdev/usehooks'
import { Check, ChevronsUpDown } from 'lucide-react'
import { useState } from 'react'
import { Button } from '@/components/ui/button'
import {
	Command,
	CommandEmpty,
	CommandGroup,
	CommandInput,
	CommandItem,
	CommandList,
} from '@/components/ui/command'
import { FormControl } from '@/components/ui/form'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { type Author, useAuthorsQuery } from '@/hooks/api/authors/useAuthorsQuery'
import { cn } from '@/lib/utils'

type Props = {
	setAuthor: (author: Author) => void
	author: Author
}

export function AuthorCombobox({ setAuthor, author }: Props) {
	const [name, setName] = useState('')
	const [open, setOpen] = useState(false)
	const debouncedName = useDebounce(name, 500)
	const { data, isLoading } = useAuthorsQuery(debouncedName)

	const onSelect = (_author: Author) => {
		setAuthor(_author)
		setOpen(false)
	}

	const authors = data?.data ?? []

	const getFullName = (_author: Author) => `${_author.firstName} ${_author.lastName}`

	// Is loading from fetch, or if debounce is currently awaiting
	const shouldDisplayLoader = isLoading || debouncedName !== name

	return (
		<Popover modal open={open} onOpenChange={setOpen}>
			<PopoverTrigger asChild>
				<FormControl>
					<Button
						variant='outline'
						role='combobox'
						className={cn('justify-between', !author && 'text-muted-foreground')}
					>
						{author.id !== -1 ? getFullName(author) : 'Select author'}
						<ChevronsUpDown className='opacity-50' />
					</Button>
				</FormControl>
			</PopoverTrigger>
			<PopoverContent className='w-56 p-0'>
				<Command shouldFilter={false}>
					<CommandInput
						placeholder='Search author...'
						className='h-9'
						value={name}
						onValueChange={setName}
						isLoading={shouldDisplayLoader}
					/>
					<CommandList>
						<CommandEmpty>
							{debouncedName === '' ? 'Start searching...' : 'No authors found...'}
						</CommandEmpty>
						<CommandGroup>
							{authors.map((listAuthor) => (
								<CommandItem
									value={listAuthor.id.toString()}
									key={listAuthor.id}
									onSelect={() => {
										onSelect(listAuthor)
									}}
								>
									{getFullName(listAuthor)}

									<Check
										className={cn(
											'ml-auto',
											listAuthor.id !== author.id
												? 'opacity-0'
												: 'opacity-100',
										)}
									/>
								</CommandItem>
							))}
						</CommandGroup>
					</CommandList>
				</Command>
			</PopoverContent>
		</Popover>
	)
}
