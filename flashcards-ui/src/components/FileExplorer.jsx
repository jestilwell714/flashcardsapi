import { useEffect, useParams, useState } from "react";
import { useNavigate } from 'react-router-dom';


export default function FileExplorer( onSelectFlashCard ) {
    const { type, id } = useParams();
    const [content,setContent] = useState(null);
    const {isLoading, setLoading} = useState(false);
    
    const fetchContentsUrl = type === "root" ? '' : `${type}/${id}`;


    useEffect(() => {
        setLoading(true);

        fetch(fetchContentsUrl)
        .then(response => response.json())
        .then(data => { 
                setContent(data);
                setLoading(false);
            }
        ).catch(err => console.error("Fetch failed:", err));
    }, [fetchContentsUrl,setLoading]); 

    const navigate = useNavigate();

    function handleClick(item) {
        if(type === "flashcard") {
            onSelectFlashCard(item)
        } else {
            navigate(`/explorer/${item.type}/${item.id}}`);
        }
    }

    return (
        isLoading ? ( 
            <div>
                <h1>Loading...</h1>
            </div> 
        ) : (
            <>
                {content.map((item) => (
                    <div key={item.id} onClick={() => handleClick(item)}>
                        <span className="icon">
                                {item.type === 'folder' ? '📁' : ''}
                                {item.type === 'deck' ? '🎴' : ''}
                        </span>
                        <h3>{item.name}</h3>
                    </div>
                ))}
            </>
        )
    );
}