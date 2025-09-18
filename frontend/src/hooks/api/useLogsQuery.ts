import useSWR from 'swr'
import { type BaseResponse, getRequest } from '@/lib/api'

type LogsResponse = BaseResponse<
	{
		id: number
		email: string
		action: string
		resource: string
		details: string | null
		timestamp: string
	}[]
>

export function useLogsQuery() {
	return useSWR<LogsResponse>('/logs', getRequest)
}
