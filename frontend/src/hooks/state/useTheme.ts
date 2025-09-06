import { create } from 'zustand'
import { persist } from 'zustand/middleware'

type ThemeStore = {
	theme: 'system' | 'dark' | 'light'
	toggleTheme: () => void
}

function getPreferredTheme() {
	return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

export const useTheme = create(
	persist<ThemeStore>(
		(set, get) => ({
			theme: 'system',
			toggleTheme: () => {
				const theme = get().theme
				let newTheme: ThemeStore['theme']

				// Theme should never be 'system' at this point, but just in case
				if (theme === 'system') {
					newTheme = getPreferredTheme()
				} else {
					newTheme = theme === 'light' ? 'dark' : 'light'
				}

				set({ theme: newTheme })
			},
		}),
		{ name: 'theme' },
	),
)

useTheme.subscribe((state) => {
	document.body.className = state.theme
})

// Set the initial theme based on the saved theme (or system preference if the localStorage field hasn't been generated yet)
const savedTheme = useTheme.getState().theme
useTheme.setState({ theme: savedTheme === 'system' ? getPreferredTheme() : savedTheme })
