import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequestWithCredentials } from '@/lib/api'

type LogoutResponse = BaseResponse<{ success: boolean }>

export function useLogout() {
	return useSWRMutation<LogoutResponse>('/auth/logout', postRequestWithCredentials)
}
