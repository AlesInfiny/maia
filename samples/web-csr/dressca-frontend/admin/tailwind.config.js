/** @type {import('tailwindcss').Config} */
import colors from "tailwindcss/colors";
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    colors:{
      black: colors.black,
      white: colors.white,
      gray: colors.gray,
      red: colors.red,
      green: colors.green,
      orange: colors.orange,
      blue: colors.blue
    },
  },
  plugins: [],
}
