import { Palette } from './pages/Palette';
import { Feed } from './pages/Feed';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { Routes, Route, Navigate } from 'react-router-dom';
import PageNotFound from 'pages/PageNotFound';
import { NewPalette } from 'pages/NewPalette';
import { About } from 'pages/About';
import { Contact } from 'pages/Contact';
import Profile from 'pages/Profile';
import LandingPage from 'pages/LandingPage';

function App() {
  return <>
    <Header />
    <div id="content">
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/profile/:id/*" element={<Profile />} />
        <Route path="/palettes">
          <Route path=":id" element={<Palette />} />
          <Route path="new" element={<NewPalette />} />
        </Route>
        <Route path="/feed">
          <Route path="new" element={<Feed />} />
          <Route path="popular" element={<Feed />} />
        </Route>
        <Route path="/about" element={<About />}/>
        <Route path="/contact" element={<Contact />} />
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </div>
    <Footer />
    </>
}

export default App;
