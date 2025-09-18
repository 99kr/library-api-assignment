import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequest } from '@/lib/api'

// dont care
type Response = BaseResponse<null>

type BooksRequest = {
	title: string
	authorId: number
	publicationYear: number
	availableCopies: number
	totalCopies: number
}

export function useBooksMutation() {
	return useSWRMutation<Response, unknown, string, BooksRequest>('/books', postRequest)
}
