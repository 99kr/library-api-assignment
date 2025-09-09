import { useSearchParams } from 'react-router'
import {
	Pagination,
	PaginationContent,
	PaginationItem,
	PaginationLink,
	PaginationNext,
	PaginationPrevious,
} from '@/components/ui/pagination'
import { useBooks } from '@/hooks/api/useBooks'
import { cn } from '@/lib/utils'

export function Books() {
	const [params, setParams] = useSearchParams()
	const page = Number(params.get('page') ?? 1) - 1
	const { data: books } = useBooks(page)

	if (!books) return null

	const totalPages = books?.data.totalPages ?? 1

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
				Showing {books?.data.numberOfElements} books out of {books?.data.totalElements}
			</p>

			<div className='grid grid-cols-4 gap-4 mt-4'>
				{books?.data.content.map((book) => (
					<div
						key={book.id}
						className='bg-card text-card-foreground px-4 py-2 rounded-[var(--radius)]'
					>
						<h2 className='text-xl font-medium'>{book.title}</h2>
						<p>{book.publicationYear}</p>
						<p>
							<span className='font-medium'>{book.availableCopies}</span> copies
							avaiable out of <span className='font-medium'>{book.totalCopies}</span>
						</p>
					</div>
				))}
			</div>

			<Pagination className='mt-4'>
				<PaginationContent>
					<PaginationItem>
						<PaginationPrevious
							onClick={previousPage}
							aria-disabled={!books?.data.hasPrevious}
							className={cn(
								!books?.data.hasPrevious && 'pointer-events-none opacity-50',
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
							aria-disabled={!books?.data.hasNext}
							className={cn(!books?.data.hasNext && 'pointer-events-none opacity-50')}
						/>
					</PaginationItem>
				</PaginationContent>
			</Pagination>
		</>
	)
}
