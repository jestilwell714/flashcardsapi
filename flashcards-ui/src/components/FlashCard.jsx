import { useState } from 'react';
import FeedBackControls from './FeedBackControls';

function BackSide({answer, onScore}) {
    return (
        <div>
            <h2>{answer}</h2>
            <FeedBackControls  onScore={onScore}/>
        </div>
    );
}


function FrontSide({question}) {
    return (
        <div>
            <h2>{question}</h2>
        </div>
    );
}

export default function FlashCard({ question, answer, onScore }) {
    const [isFlipped, setFlipped] = useState(false);

    return (
        <div onClick={() => !isFlipped && setFlipped(true)}>
            {isFlipped ? (
                <BackSide answer={answer} onScore={onScore} />
            ) : (
                <FrontSide question={question} />
            )}
        </div>
    );
}

