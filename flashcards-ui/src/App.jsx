import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Explorer from './pages/Explorer';
import CramMode from './components/CramMode';

function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/explorer/root/0" replace />} />
        <Route path="/cram/:type/:id" element={<Explorer />} />
        <Route path="/explorer/:type/:id" element={<Explorer />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
