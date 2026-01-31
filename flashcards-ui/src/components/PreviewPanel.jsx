import { useNavigate } from 'react-router-dom';

export default function PreviewPanel( item, type, onPlay, onCreate, onEdit) { 
    const navigate = useNavigate();
    const cramModeUrl = type === "root" ? 'https://localhost:8080/api/cram' : `https://localhost:8080/api/cram${type}/${item.id}`;

    function handleCreate() {
        onCreate();
    }

    function handleEdit() {
        onEdit();
    }

    function handlePlay() {
        navigate({cramModeUrl})
        onPlay();
    }

    return (
        <div>
            <h2>item.name</h2>
            
            <button onClick={ handlePlay }>Play</button>
            <button onClick={ handleCreate }>Create</button>
            <button onClick={ handleEdit }>Edit</button>
        </div>
    );
}