import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs))
}

const padToDoubleDigits = (x: number) => x.toString().padStart(2, '0')

export function msToMinutesAndSeconds(milliseconds: number) {
	const totalSeconds = Math.floor(milliseconds / 1000)
	const minutes = Math.floor((totalSeconds / 60) % 60)
	const seconds = Math.floor(totalSeconds % 60)

	return `${padToDoubleDigits(minutes)}:${padToDoubleDigits(seconds)}`
}

export function msToDaysHoursMinutesAndSeconds(milliseconds: number) {
	const totalSeconds = Math.floor(milliseconds / 1000)
	const days = Math.floor(totalSeconds / 60 / 60 / 24)
	const hours = Math.floor((totalSeconds / 60 / 60) % 24)
	const minutes = Math.floor((totalSeconds / 60) % 60)
	const seconds = Math.floor(totalSeconds % 60)

	return `${padToDoubleDigits(days)}:${padToDoubleDigits(hours)}:${padToDoubleDigits(minutes)}:${padToDoubleDigits(seconds)}`
}
