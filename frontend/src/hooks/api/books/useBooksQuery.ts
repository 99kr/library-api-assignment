import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

type BooksResponse = BaseResponse<{
	content: {
		id: number
		title: string
		publicationYear: number
		availableCopies: number
		totalCopies: number
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

export function useBooksQuery(page: number) {
	return useSWR<BooksResponse>(`/books?page=${page}`, getRequest)
}
