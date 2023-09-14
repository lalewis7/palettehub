import Header from './components/header/header'
import './bootstrap.scss'
import './globals.scss'
import { Nunito } from 'next/font/google'

const nunito = Nunito({
  subsets: ['latin'],
  display: 'swap',
})

export const metadata = {
  title: 'Palette Hub - Color palettes for web developers, artists, and color enthusiasts',
  description: 'Palette Hub allows you to inspire your creativty and put it to use by browsing, curating, and sharing color palettes of any and all aesthetics.',
}

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={nunito.className}>
        <div id="root">
          <Header />
          {children}
        </div>
      </body>
    </html>
  )
}
