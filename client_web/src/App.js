import { Palette } from './components/Palette';
import { Feed } from './components/Feed';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { Routes, Route, Navigate } from 'react-router-dom';
import PageNotFound from 'components/PageNotFound';
import { NewPalette } from 'components/NewPalette';

function App() {
  return <>
    <Header />
    <div id="content">
      <Routes>
        <Route path="/" element={<Navigate to="/feed/new" />} />
        <Route path="/palettes">
          <Route path=":id" element={<Palette />} />
          <Route path="new" element={<NewPalette />} />
        </Route>
        <Route path="/feed">
          <Route path="new" element={<Feed />} />
          <Route path="popular" element={<Feed />} />
        </Route>
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </div>
    <Footer />
    </>
}

export default App;
