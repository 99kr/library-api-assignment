import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequest } from '@/lib/api'

type LoginResponse = BaseResponse<{ sessionId: string }>
type LoginRequest = { email: string; password: string }

export function useLogin() {
	return useSWRMutation<LoginResponse, unknown, string, LoginRequest>('/auth/login', postRequest)
}
