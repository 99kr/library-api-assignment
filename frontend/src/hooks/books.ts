import type { Author } from '@/hooks/authors'
import { useMutation, useQuery } from '@/hooks/swr'
import type { Pageable } from '@/lib/api'

type Book = {
	id: number
	title: string
	publicationYear: number
	availableCopies: number
	totalCopies: number
}

type BooksResponse = {
	content: Book[]
	pageable: Pageable
}

type BookWithAuthor = Book & { author: Author }

type BooksDetailedResponse = {
	content: BookWithAuthor[]
	pageable: Pageable
}

type PostBooksRequest = {
	title: string
	authorId: number
	publicationYear: number
	availableCopies: number
	totalCopies: number
}

export const useBooks = (page: number) => useQuery<BooksResponse>(`/books?page=${page}`)

export const useBooksDetailed = (page: number) =>
	useQuery<BooksDetailedResponse>(`/books/detailed?page=${page}`)

export const useCreateBook = () => useMutation<void, PostBooksRequest>('/books')
