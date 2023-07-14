import { Palette } from './components/Palette';
import { Feed } from './components/Feed';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { Routes, Route, Navigate } from 'react-router-dom';

function App() {
  return <>
    <Header />
    <div id="content">
      <Routes>
        <Route path="/" element={<Navigate to="/palettes/new" />} />
        <Route path="/palettes">
          <Route path=":id" element={<Palette />} />
          <Route path="new" element={<Feed />} />
        </Route>
      </Routes>
    </div>
    <Footer />
    </>
}

export default App;
