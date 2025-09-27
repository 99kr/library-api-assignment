import { useQuery } from '@/hooks/swr'

export type Loan = {
	id: number
	state: 'BORROWED' | 'EXPIRED' | 'RETURNED' | 'RETURNED_LATE'
	borrowedAt: number
	dueAt: number
	returnedAt: number | null
}

type UserLoansResponse = Loan & {
	book: {
		id: number
		title: string
	}
}

export const useUserLoans = (id: number | null) =>
	useQuery<UserLoansResponse[]>(id ? `/users/${id}/loans` : null)
