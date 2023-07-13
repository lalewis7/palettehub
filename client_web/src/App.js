import { Palette } from './components/Palette';
import { Feed } from './components/Feed';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { Routes, Route, Navigate } from 'react-router-dom';

function App() {
  return <>
    <Header />
    <Routes>
      <Route path="/" element={<Navigate to="/palettes/new" />} />
      <Route path="/palettes">
        <Route path=":id" element={<Palette />} />
        <Route path="new" element={<Feed />} />
      </Route>
    </Routes>
    <Footer />
    </>
}

export default App;
