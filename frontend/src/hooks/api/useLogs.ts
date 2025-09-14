import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

type LogsResponse = BaseResponse<
	{
		id: number
		email: string
		action: string
		resource: string
		timestamp: string
	}[]
>

export function useLogs() {
	return useSWR<LogsResponse>('/logs', getRequest)
}
