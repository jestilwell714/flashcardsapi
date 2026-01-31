import { useParams } from "react";
import { useNavigate } from 'react-router-dom';

export default function PreviewPanel( ) { 
    const { type, id } = useParams();
    const navigate = useNavigate();
    const cramModeUrl = type === "root" ? '' : `${type}/${id}`;

    return (
        <div>
            <h2></h2>
            
            <button onClick={ navigate({cramModeUrl}) }>Play</button>
        </div>
    );
}