import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequest } from '@/lib/api'

type RegisterResponse = BaseResponse<{ success: boolean }>
type RegisterRequest = { firstName: string; lastName: string; email: string; password: string }

export function useRegister() {
	return useSWRMutation<RegisterResponse, unknown, string, RegisterRequest>(
		'/auth/register',
		postRequest,
	)
}
