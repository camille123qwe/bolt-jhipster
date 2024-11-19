/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: 'var(--color-primary)',
        hover: 'var(--color-hover)',
        auxiliary: 'var(--color-auxiliary)',
        'auxiliary-2': 'var(--color-auxiliary-2)',
        'auxiliary-3': 'var(--color-auxiliary-3)',
        warning: 'var(--color-warning)',
        disabled: 'var(--color-disabled)',
        divider: 'var(--color-divider)',
        background: 'var(--color-background)',
        'text-primary': 'var(--color-text-primary)',
        'text-secondary': 'var(--color-text-secondary)',
        'text-auxiliary': 'var(--color-text-auxiliary)',
        'text-hint': 'var(--color-text-hint)',
      },
      fontFamily: {
        sans: ['Source Han Sans CN', 'sans-serif'],
      },
      fontSize: {
        xs: ['12px', '1.5'],
        sm: ['14px', '1.5'],
        base: ['16px', '1.5'],
        lg: ['18px', '1.5'],
        xl: ['20px', '1.5'],
        '2xl': ['24px', '1.5'],
        '3xl': ['36px', '1.5'],
      },
    },
  },
  plugins: [],
}