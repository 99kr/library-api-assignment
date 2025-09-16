import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

type BooksResponse = BaseResponse<{
	content: {
		id: number
		title: string
		publicationYear: number
		availableCopies: number
		totalCopies: number
		author: {
			id: number
			firstName: string
			lastName: string
			birthYear: number
			nationality: string
		}
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

export function useBooksDetailed(page: number) {
	return useSWR<BooksResponse>(`/books/detailed?page=${page}`, getRequest)
}
