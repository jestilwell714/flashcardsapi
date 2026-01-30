export default function FeedBackControls({onScore}) {

    const handleButtonClick = (e, score) => {
        e.stopPropagation(); 
        onScore(score);      
    };

    return (
        <div>
            <button onClick={(e) => handleButtonClick(e,1)}>Wrong</button>
            <button onClick={(e) => handleButtonClick(e,2)}>Bad</button>
            <button onClick={(e) => handleButtonClick(e,3)}>Okay</button>
            <button onClick={(e) => handleButtonClick(e,4)}>Good</button>
        </div>
    )
}

