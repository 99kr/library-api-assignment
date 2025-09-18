import useSWR from 'swr'
import type { Author } from '@/hooks/api/authors/useAuthorsQuery'
import { type BaseResponse, getRequest } from '@/lib/api'

type BooksResponse = BaseResponse<{
	content: {
		id: number
		title: string
		publicationYear: number
		availableCopies: number
		totalCopies: number
		author: Author
	}[]
	pageable: {
		totalPages: number
		totalElements: number
		numberOfElements: number
		size: number
		last: boolean
		first: boolean
		hasPrevious: boolean
		hasNext: boolean
	}
}>

export function useBooksDetailedQuery(page: number) {
	return useSWR<BooksResponse>(`/books/detailed?page=${page}`, getRequest)
}
