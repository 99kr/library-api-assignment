import { useSearchParams } from 'react-router'
import { Badge } from '@/components/ui/badge'
import {
	Pagination,
	PaginationContent,
	PaginationItem,
	PaginationLink,
	PaginationNext,
	PaginationPrevious,
} from '@/components/ui/pagination'
import { useBooksDetailed } from '@/hooks/api/books/useBooksDetailed'
import { cn } from '@/lib/utils'

export function Books() {
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
			<h1 className='text-4xl font-semibold'>Books</h1>
			<p>
				Showing {pageable.numberOfElements} books out of {pageable.totalElements}
			</p>

			<div className='grid grid-cols-4 gap-4 mt-4'>
				{books.map((book) => (
					<div
						key={book.id}
						className='bg-card text-card-foreground px-4 py-2 rounded-[var(--radius)]'
					>
						<div className='flex justify-between gap-2'>
							<h2 className='text-xl font-medium'>{book.title}</h2>
							<p>{book.publicationYear}</p>
						</div>
						<p className='mb-2'>
							{book.author.firstName} {book.author.lastName}
						</p>

						{book.availableCopies > 0 ? (
							<Badge className='bg-green-400/40'>
								{book.availableCopies} in stock
							</Badge>
						) : (
							<Badge variant='destructive'>Out of stock</Badge>
						)}
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
