import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs))
}

const padToDoubleDigits = (x: number) => x.toString().padStart(2, '0')

export function millisecondsToReadableTime(milliseconds: number) {
	const minutes = Math.floor((milliseconds / 1000 / 60 / 60) * 60)
	const seconds = Math.floor(((milliseconds / 1000 / 60 / 60) * 60 - minutes) * 60)

	return `${padToDoubleDigits(minutes)}:${padToDoubleDigits(seconds)}`
}
