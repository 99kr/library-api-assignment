import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

export type Author = {
	id: number
	firstName: string
	lastName: string
	birthYear: number
	nationality: string
}

type AuthorsResponse = BaseResponse<Author[]>

export function useAuthorsQuery(name?: string) {
	return useSWR<AuthorsResponse>(name ? `/authors?name=${name}` : null, getRequest, {})
}
