import useSWR from 'swr'
import useSWRMutation from 'swr/mutation'
import { type BaseResponse, getRequest, postRequest, postRequestWithCredentials } from '@/lib/api'

export function useQuery<TResponse = void>(key?: string | null) {
	return useSWR<BaseResponse<TResponse>>(key, getRequest)
}

export function useMutation<TResponse = void, TRequest = void>(key: string) {
	return useSWRMutation<BaseResponse<TResponse>, unknown, string, TRequest>(key, postRequest)
}

export function useMutationWithCredentials<TResponse = void, TRequest = void>(key: string) {
	return useSWRMutation<BaseResponse<TResponse>, unknown, string, TRequest>(
		key,
		postRequestWithCredentials,
	)
}
