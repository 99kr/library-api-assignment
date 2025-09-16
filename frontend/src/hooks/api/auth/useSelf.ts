import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

export type SelfResponse = BaseResponse<{
	firstName: string
	lastName: string
	email: string
	isLoggedIn: boolean
}>

export const loggedOutState: SelfResponse = {
	data: { firstName: '', lastName: '', email: '', isLoggedIn: false },
	errors: [],
}

export function useSelf() {
	return useSWR<SelfResponse>('/auth/self', getRequest)
}
