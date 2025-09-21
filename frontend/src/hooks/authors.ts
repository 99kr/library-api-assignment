import { useQuery } from '@/hooks/swr'

export type Author = {
	id: number
	firstName: string
	lastName: string
	birthYear: number
	nationality: string
}

export const useAuthors = (name?: string) =>
	useQuery<Author[]>(name ? `/authors?name=${name}` : null)
