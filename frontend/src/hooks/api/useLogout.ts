import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequest } from '@/lib/api'

type LogoutResponse = BaseResponse<{ success: boolean }>

export function useLogout() {
	return useSWRMutation<LogoutResponse>('/auth/logout', postRequest)
}
