import { Plus } from 'lucide-react'
import { useSearchParams } from 'react-router'
import { CreateBookDialog } from '@/components/dialogs/create-book-dialog'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import {
	Pagination,
	PaginationContent,
	PaginationItem,
	PaginationLink,
	PaginationNext,
	PaginationPrevious,
} from '@/components/ui/pagination'
import { useBooksDetailed } from '@/hooks/books'
import { useJwt } from '@/hooks/state/useJwt'
import { cn, getRandomImgUrl } from '@/lib/utils'

export function Books() {
	const isAdmin = useJwt((state) => state.hasRole('ADMIN'))
	const [params, setParams] = useSearchParams()
	const page = Number(params.get('page') ?? 1) - 1
	const { data } = useBooksDetailed(page)

	if (!data) return null

	const { content: books, pageable } = data.data

	const totalPages = pageable.totalPages ?? 1

	function setPage(pageNumber: number) {
		if (pageNumber < 0 || pageNumber > totalPages - 1) return
		setParams((prev) => ({ ...prev, page: pageNumber + 1 }))
	}

	function nextPage() {
		setPage(page + 1)
	}

	function previousPage() {
		setPage(page - 1)
	}

	return (
		<>
			<div className='flex justify-between items-center'>
				<div>
					<h1 className='text-4xl font-semibold'>Books</h1>
					<p>
						Showing {pageable.numberOfElements} books out of {pageable.totalElements}
					</p>
				</div>
				{isAdmin && (
					<CreateBookDialog>
						<Button>
							<Plus />
							Add book
						</Button>
					</CreateBookDialog>
				)}
			</div>

			<div className='grid grid-cols-4 gap-4 mt-4'>
				{books.map((book) => (
					<div
						key={book.id}
						className='bg-card text-card-foreground px-4 py-4 rounded-[var(--radius)]'
					>
						<img
							alt={book.title}
							className='w-full h-52 object-cover rounded-md mb-2 bg-gradient-to-bl from-background to-card'
							loading='lazy'
							src={getRandomImgUrl(book.title)}
						/>

						<div className='flex justify-between gap-2'>
							<h2 className='text-xl font-medium'>{book.title}</h2>
							<p>{book.publicationYear}</p>
						</div>
						<p className='mb-2'>
							{book.author.firstName} {book.author.lastName}
						</p>

						<Badge variant={book.availableCopies > 0 ? 'green' : 'red'}>
							{book.availableCopies > 0
								? `${book.availableCopies} in stock`
								: 'Out of stock'}
						</Badge>
					</div>
				))}
			</div>

			<Pagination className='mt-4'>
				<PaginationContent>
					<PaginationItem>
						<PaginationPrevious
							onClick={previousPage}
							aria-disabled={!pageable.hasPrevious}
							className={cn(
								!pageable.hasPrevious && 'pointer-events-none opacity-50',
							)}
						/>
					</PaginationItem>

					{Array.from({ length: totalPages }, (_, i) => (
						<PaginationItem key={i}>
							<PaginationLink isActive={page === i} onClick={() => setPage(i)}>
								{i + 1}
							</PaginationLink>
						</PaginationItem>
					))}

					<PaginationItem>
						<PaginationNext
							onClick={nextPage}
							aria-disabled={!pageable.hasNext}
							className={cn(!pageable.hasNext && 'pointer-events-none opacity-50')}
						/>
					</PaginationItem>
				</PaginationContent>
			</Pagination>
		</>
	)
}
