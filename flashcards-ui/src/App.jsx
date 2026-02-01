import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Explorer from './components/FileExplorer';

function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/explorer/root/0" replace />} />
      
        <Route path="/explorer/:type/:id" element={<Explorer />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
